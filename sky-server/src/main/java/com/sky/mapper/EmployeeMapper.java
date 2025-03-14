package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

@ApiOperation("新增员工")
    @Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user ) VALUE (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
     void insert(Employee employee);

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

@ApiOperation("分页查询")
    Page<Employee> limit(EmployeePageQueryDTO employeePageQueryDTO);

@ApiOperation("修改员工")
    void update(Employee employee);

@ApiOperation("根据ID查询员工信息")
@Select("select * from employee where id = #{id}")
    Employee getById(long id);
}
