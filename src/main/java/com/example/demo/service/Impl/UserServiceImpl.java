package com.example.demo.service.Impl;

import com.example.demo.dto.ChangeStatuFlagDTO;
import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IUserService;
import com.example.demo.util.StatusEnum;
import com.example.demo.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;


/**
 * @Description:
 * @Author: boolean
 * @Date: 2020/2/1 14:34
 */
@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;

    public User getUserInfo(User user) {
        return userMapper.getUserInfo(user);
    }

    @Override
    public User getUser(String name, String password) {
        log.info("name为{}, password为{}", name, password);
        return userMapper.getUser(name, password);
    }

    @Override
    public User getUserByName(String name) {
        return userMapper.getUserByName(name);
    }

    @Override
    public UserVo getUserPage(UserDto dto) {
        String name = Optional.ofNullable(dto.getUser()).map(User::getName).orElse(null);
        String adress = Optional.ofNullable(dto.getUser()).map(User::getAdress).orElse(null);
        Integer start = (dto.getPagenum() - 1) * dto.getSize();
        Integer size = dto.getSize();
        List<User> userPage = userMapper.getUserPage(name, start, size);
        userPage.forEach(user -> {
            if (user.getStatus() == StatusEnum.RIGHT.getCode()) {
                user.setStatuFlag(true);
            }
            if (user.getStatus() == StatusEnum.WRONG.getCode()) {
                user.setStatuFlag(false);
            }
        });
        Integer total = userMapper.getTotal(name, start, size);
        UserVo userVo = new UserVo();
        userVo.setUserList(userPage);
        userVo.setTotal(total);
        return userVo;

    }

    @Override
    public Boolean changeStatuFlag(ChangeStatuFlagDTO changeStatuFlagDTO) {
        Integer flag = changeStatuFlagDTO.getStatus();
        Integer id = changeStatuFlagDTO.getId();
        return userMapper.changeStatuFlag(flag, id);
    }

}



