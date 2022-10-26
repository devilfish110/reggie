package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.common.ObjectConversionObject;
import com.study.reggie.entities.Category;
import com.study.reggie.entities.Setmeal;
import com.study.reggie.entities.SetmealDish;
import com.study.reggie.entities.dto.SetmealDto;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.mapper.SetmealMapper;
import com.study.reggie.service.CategoryService;
import com.study.reggie.service.SetmealDishService;
import com.study.reggie.service.SetmealService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author That's all
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2022-10-16 14:35:31
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public Page<SetmealDto> getPage(Integer page, Integer pageNum, String name) {
        //1.查询setmeal分页
        Page<Setmeal> setmealPage = new Page<>(page, pageNum);
        //由更新时间降序排列
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.like(StringUtils.isNotBlank(name), Setmeal::getName, name)
                .orderByDesc(Setmeal::getUpdateTime);
        super.page(setmealPage, setmealQueryWrapper);
        //创建最终结果page<setmealDto>
        Page<SetmealDto> setmealDtoPage = new Page<>();
        //把除了records（前端需要的数据）的属性拿过来
        setmealDtoPage.setTotal(setmealPage.getTotal());
        setmealDtoPage.setCurrent(setmealPage.getCurrent());
        setmealDtoPage.setSize(setmealPage.getSize());
        setmealDtoPage.setOrders(setmealPage.getOrders());
        //最后处理records
        ArrayList<SetmealDto> setmealDtoList = new ArrayList<>();
        /*
          关系：每一个setmealDto都缺少对应的setmealDishes属性和categoryName属性
          1.setmealDish表中有setmeal_id与setmeal表中的id对应
          2.setmeal表中有categoryId与category表中的id对应
         */
        //获取setmeal,并转为setmealDto
        List<Setmeal> setmealList = setmealPage.getRecords();
        //获取setmealDishes和categoryName
        for (Setmeal setmeal : setmealList) {
            if (setmeal != null) {
                //执行转换
                SetmealDto setmealDto = ObjectConversionObject.convertSetmealVo(setmeal);
                //每一个setmealDto都缺少对应的setmealDishes属性和categoryName属性
                //获取每个setmeal对应的setmealDishes
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId())
                        .orderByDesc(SetmealDish::getUpdateTime);
                List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
                //补全缺失的setmealDishes
                setmealDto.setSetmealDishes(setmealDishes);
                //获取categoryName属性
                Long categoryId = setmealDto.getCategoryId();
                Category category = categoryService.getById(categoryId);
                //补全categoryName属性
                setmealDto.setCategoryName(category.getName());
                //把得到的最终成品setmealDto添加到list中
                setmealDtoList.add(setmealDto);
            } else {
                return null;
            }
        }
        //给最终结果records赋值
        if (!setmealDtoList.isEmpty()) {
            setmealDtoPage.setRecords(setmealDtoList);
        } else {
            setmealDtoPage.setRecords(null);
        }
        return setmealDtoPage;
    }


    @Override
    public boolean saveSetmealAndDish(SetmealDto setmealDto) throws CustomerException {
        if (setmealDto != null) {
            //套餐名称存在唯一约束
            String setmealName = setmealDto.getName();
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getName, setmealName);
            //查询该套餐名是否存在
            Long count = super.count(queryWrapper);
            queryWrapper.clear();
            if (Objects.equals(count, 0L)) {
                //定义添加标记
                boolean saveSetmealDishes = false;
                //1.保存setmeal信息
                boolean saveSetmeal = super.save(setmealDto);
                //setmealDto中的主键:setmealID时雪花算法生成的
                //setmeal表中存在唯一索引name(setmealDto中就存在),所以可以获取setmealId
                //创建setmeal对象接收查到的数据
                if (saveSetmeal) {
                    //保存setmeal成功再去查setmeal
                    queryWrapper.eq(Setmeal::getName, setmealName);
                    //获取到了当前插入的setmeal信息,从而获取setmealId
                    Setmeal setmeal = super.getOne(queryWrapper);
                    //2.保存setmealDish表信息
                    List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
                    //setmealDish中缺少setmealId信息
                    for (SetmealDish setmealDish : setmealDishes) {
                        setmealDish.setSetmealId(String.valueOf(setmeal.getId()));
                    }
                    //保存setmealDish
                    saveSetmealDishes = setmealDishService.saveBatch(setmealDishes);
                }
                return saveSetmeal && saveSetmealDishes;
            } else {
                throw new CustomerException("套餐名称：" + setmealName + "，已存在");
            }
        }
        return false;
    }

    @Override
    public SetmealDto getSetmealAndDishById(Long id) {
        if (id != null) {
            //1.查询setmeal信息
            Setmeal setmeal = super.getById(id);
            if (setmeal != null) {
                //2.查询setmealDish信息(关联：setmeaiId)
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId, id)
                        .orderByDesc(SetmealDish::getUpdateTime);
                List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
                //把两个信息封装组合为SetmealDish
                SetmealDto setmealDto = ObjectConversionObject.convertSetmealVo(setmeal);
                //补全SetmealDishes
                setmealDto.setSetmealDishes(setmealDishes);
                //补全categoryName（setmeal中有=categoryId）
                Category category = categoryService.getById(setmeal.getCategoryId());
                setmealDto.setCategoryName(category.getName());
                return setmealDto;
            }
        }
        return null;
    }


    @Override
    public boolean updateSetmealAndDishById(SetmealDto setmealDto) {
        if (setmealDto != null) {
            //1.修改setmealDish表中的信息
            //setmeal表中的主键id对应setmealDish表中的字段setmeal_id
            //先查询setmealDish相关信息
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
            List<SetmealDish> list = setmealDishService.list(queryWrapper);
            //判断setmealDish有没有相关信息
            if (!list.isEmpty()) {
                //删除原来setmealDish表中有关信息
                boolean removeSetmealDish = setmealDishService.removeByIds(list);
                if (!removeSetmealDish) {
                    return false;
                }
            }
            //没有信息，直接添加
            //传进来的参数setmealDto中属性setmealDishes缺少一个值setmeal_id
            List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
            //补全setmealDishes
            setmealDishes.forEach(setmealDish -> setmealDish.setSetmealId(String.valueOf(setmealDto.getId())));
            //执行保存
            boolean saveSetmealDish = setmealDishService.saveBatch(setmealDishes);
            //2.修改setmeal表中的信息
            boolean updateSetmeal = super.updateById(setmealDto);
            return saveSetmealDish && updateSetmeal;
        }
        return false;
    }


    @Override
    public boolean updateSetmealStatusByIds(List<Long> ids, Integer status) {
        if (!ids.isEmpty() && status != null) {
            List<Setmeal> setmeals = super.listByIds(ids);
            //给这些数据设置新的status
            setmeals.forEach(setmeal -> setmeal.setStatus(status));
            //执行保存
            return super.updateBatchById(setmeals);
        }
        return false;
    }


    @Override
    public boolean removeSetmealAndSetmealDish(List<Long> ids) {
        if (!ids.isEmpty()) {
            //1.删除setmealDish表中的信息
            ids.forEach(id ->
            {
                LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(SetmealDish::getSetmealId, id);
                setmealDishService.remove(queryWrapper);
            });
            //2.删除setmeal表中的信息
            super.removeByIds(ids);
            return true;
        }
        return false;
    }


    @Override
    public List<SetmealDto> getSetmealAndDishList(Long categoryId, Integer status) {
        //创建最终返回对象
        ArrayList<SetmealDto> list = new ArrayList<>();
        //1.获取setmeal信息
        LambdaQueryWrapper<Setmeal> setmealQueryWrapper = new LambdaQueryWrapper<>();
        setmealQueryWrapper.eq(Setmeal::getCategoryId, categoryId)
                .eq(Setmeal::getStatus, status)
                .orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = super.list(setmealQueryWrapper);
        if (setmealList != null) {
            //2.获取setmealDish信息
            for (Setmeal setmeal : setmealList) {
                Long setmealId = setmeal.getId();
                LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
                setmealDishQueryWrapper.eq(SetmealDish::getSetmealId, setmealId)
                        .orderByDesc(SetmealDish::getUpdateTime);
                List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishQueryWrapper);
                //把每个setmeal转为setmealDto
                SetmealDto setmealDto = ObjectConversionObject.convertSetmealVo(setmeal);
                if (!setmealDishes.isEmpty()) {
                    //补全缺失的setmealDishes
                    setmealDto.setSetmealDishes(setmealDishes);
                }
                //添加到最终结果集
                list.add(setmealDto);
            }
        }
        return list;
    }
}




