package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对密码进行加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    @Override
    @ApiOperation("新增员工")
    public void save(EmployeeDTO employeeDTO) {
        //将DTO转化为实体类BeanUtils
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        //将剩下的属性赋值
        //状态
        employee.setStatus(StatusConstant.ENABLE);
        //密码
        employee.setPassword(PasswordConstant.DEFAULT_PASSWORD);
        //创建和修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        //创建用户和修改用户
        employee.setCreateUser(BaseContext.getCurrentId());
        employee.setUpdateUser(BaseContext.getCurrentId());

        employeeMapper.insert(employee);
    }

    @Override
    @ApiOperation("分页查询")
    public PageResult pagequery(EmployeePageQueryDTO employeePageQueryDTO) {
        //使用插件分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        //返回值固定为Page<Employee>
        Page<Employee> page = employeeMapper.limit(employeePageQueryDTO);
        //获取数据
        long total = page.getTotal();
        List<Employee> records = page.getResult();
        return new PageResult(total,records);
    }

    @Override
    @ApiOperation("修改员工")
    public void StatusOrStop(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .updateTime(LocalDateTime.now())
                .build();
        employeeMapper.update(employee);
    }

    @Override
    @ApiOperation("根据ID查询数据")
    public Employee getById(long id) {
        Employee employee = employeeMapper.getById(id);
        employee.setPassword("********");
        return employee;
    }

    @Override
    @ApiOperation("修改员工信息")
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        //拷贝属性
        BeanUtils.copyProperties(employeeDTO,employee);
        //修改没有拷贝的属性
        employee.setStatus(StatusConstant.ENABLE);
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());
        //调用修改的方法
        employeeMapper.update(employee);
    }


}
