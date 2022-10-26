package com.study.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.reggie.entities.AddressBook;
import com.study.reggie.exception.CustomerException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author That's all
 * @description 针对表【address_book(地址管理)】的数据库操作Service
 * @createDate 2022-10-17 20:32:00
 */
@Transactional(rollbackFor = Exception.class)
public interface AddressBookService extends IService<AddressBook> {

    /**
     * 获取user的address列表
     *
     * @param id user的主键Id
     * @return address列表
     */
    List<AddressBook> getAddressListById(Long id);

    /**
     * 添加user的address
     *
     * @param id      user的id
     * @param address addressBook信息
     * @return 添加结果
     */
    boolean saveUserAddressById(Long id, AddressBook address);

    /**
     * 修改默认地址
     *
     * @param addressId 默认地址的id
     * @param userId    用户id
     * @return 修改结果
     */
    boolean updateDefaultAddress(Long addressId, Long userId);

    /**
     * 获取用户默认的address
     *
     * @param userId 用户id
     * @return address
     * @throws CustomerException 自定义异常处理
     */
    AddressBook getDefaultAddressByUserId(Long userId) throws CustomerException;
}
