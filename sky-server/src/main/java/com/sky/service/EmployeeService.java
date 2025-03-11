package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import io.swagger.annotations.ApiOperation;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    @ApiOperation("新增员工")
    void save(EmployeeDTO employeeDTO);

    @ApiOperation("分页查询")
    PageResult pagequery(EmployeePageQueryDTO employeePageQueryDTO);

    @ApiOperation("启用和禁用账号")
    void StatusOrStop(Integer status, Long id);
}
