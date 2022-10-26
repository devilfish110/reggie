package com.study.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.reggie.entities.AddressBook;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.mapper.AddressBookMapper;
import com.study.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author That's all
 * @description 针对表【address_book(地址管理)】的数据库操作Service实现
 * @createDate 2022-10-17 20:32:00
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
        implements AddressBookService {

    @Override
    public List<AddressBook> getAddressListById(Long id) {
        if (id != null) {
            LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBook::getUserId, id)
                    .orderByDesc(AddressBook::getUpdateTime);
            List<AddressBook> list = super.list(queryWrapper);
            queryWrapper.clear();
            return list;
        }
        return null;
    }

    @Override
    public boolean saveUserAddressById(Long id, AddressBook address) {
        if (id != null && address != null) {
            //把address中缺少的userId补全
            address.setUserId(id);
            return super.save(address);
        }
        return false;
    }

    @Override
    public boolean updateDefaultAddress(Long addressId, Long userId) {
        if (userId != null && addressId != null) {
            //查询user的所有地址
            LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBook::getUserId, userId);
            List<AddressBook> addressList = super.list(queryWrapper);
            if (addressList != null) {
                for (AddressBook addressBook : addressList) {
                    Long id = addressBook.getId();
                    //判断当前id是否是传来的addressId
                    if (id.equals(addressId)) {
                        addressBook.setIsDefault(1);
                    } else {
                        //不是的地址都设置不是默认地址
                        addressBook.setIsDefault(0);
                    }
                }
                return super.updateBatchById(addressList);
            }
        }
        return false;
    }


    @Override
    public AddressBook getDefaultAddressByUserId(Long userId) throws CustomerException {
        if (userId != null) {
            LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(AddressBook::getUserId, userId);
            return super.getOne(queryWrapper);
        } else {
            throw new CustomerException("您暂未登录，请登陆后查看");
        }
    }
}




