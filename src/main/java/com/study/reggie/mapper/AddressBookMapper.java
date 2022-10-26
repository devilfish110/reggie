package com.study.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.study.reggie.entities.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ChenFeng
 * @description 针对表【address_book(地址管理)】的数据库操作Mapper
 * @createDate 2022-10-17 20:32:00
 * @Entity com.study.reggie.entities.AddressBook
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {

}




