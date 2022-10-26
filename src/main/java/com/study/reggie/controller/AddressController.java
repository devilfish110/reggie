package com.study.reggie.controller;

import com.study.reggie.common.R;
import com.study.reggie.entities.AddressBook;
import com.study.reggie.exception.CustomerException;
import com.study.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author That's all
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressController {

    @Autowired
    private AddressBookService addressService;

    /**
     * 获取用户的地址信息列表
     *
     * @param session 获取userId
     * @return List<address>
     */
    @RequestMapping("/list")
    public R<List<AddressBook>> userAddress(HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        List<AddressBook> addresses = addressService.getAddressListById(userId);
        return R.success(addresses);
    }

    /**
     * 添加用户的地址信息
     *
     * @param address 地址信息
     * @param session 获取当前用户的信息
     * @return 添加结果
     */
    @PostMapping("")
    public R<String> addUserAddress(@RequestBody AddressBook address, HttpSession session) {
        Long userId = (Long) session.getAttribute("user");
        boolean saveUserAddress = addressService.saveUserAddressById(userId, address);
        return saveUserAddress ? R.success("添加成功") : R.error("添加失败");
    }

    /**
     * 修改默认的地址
     *
     * @param address 地址的id
     * @return 修改结果
     */
    @PutMapping("/default")
    public R<String> updateUserAddress(@RequestBody AddressBook address, HttpSession session) {
        //获取当前登录的userID
        Long userId = (Long) session.getAttribute("user");
        Long addressId = address.getId();
        if (userId != null && addressId != null) {
            boolean updateDefaultAddress = addressService.updateDefaultAddress(addressId, userId);
            return updateDefaultAddress ? R.success("修改成功") : R.error("修改失败");
        }
        return R.error("请求参数为空");
    }

    /**
     * 编辑地址信息时先查询地址
     *
     * @param id 地址id
     * @return 地址信息
     */
    @GetMapping("/{addressId}")
    public R<AddressBook> getUserAddress(@PathVariable("addressId") Long id) {
        if (id != null) {
            AddressBook addressBook = addressService.getById(id);
            return R.success(addressBook);
        }
        return R.error("请求参数为空");
    }

    /**
     * 修改用户的地址信息
     *
     * @param address 新的地址信息
     * @return 修改结果
     */
    @PutMapping("")
    public R<String> editAddress(@RequestBody AddressBook address) {
        if (address != null) {
            boolean update = addressService.updateById(address);
            return update ? R.success("修改成功") : R.error("修改失败");
        }
        return R.error("传入的参数为空");
    }

    /**
     * 删除用户的地址信息
     *
     * @param addressId 地址ID
     * @return 删除结果
     */
    @DeleteMapping("")
    public R<String> deleteAddress(@RequestParam("ids") Long addressId) {
        if (addressId != null) {
            boolean removeAddress = addressService.removeById(addressId);
            return removeAddress ? R.success("删除成功") : R.error("删除失败");
        }
        return R.error("传入的参数为空");
    }

    @GetMapping("/default")
    public R<AddressBook> getUserDefaultAddress(HttpSession session) throws CustomerException {
        Long userId = (Long) session.getAttribute("user");
        AddressBook address = addressService.getDefaultAddressByUserId(userId);
        return R.success(address);
    }
}
