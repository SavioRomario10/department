package model.dao.impl;

import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.dao.DepartmentDao;

public class DepartmentDaoJDBC implements DepartmentDao {

  private Connection conn;

  public DepartmentDaoJDBC() {}
  public DepartmentDaoJDBC(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void insert(Department obj) {
     PreparedStatement st = null;
    try{
      st = conn.prepareStatement("INSERT INTO department (DepartmentId, DepartmentName) VALUES (?, ?)");

      st.setInt(1, obj.getId());
      st.setString(2, obj.getName());

      int rowsAffected = st.executeUpdate();

      if(rowsAffected > 0){
        ResultSet rs = st.getGeneratedKeys();
        if(rs.next()){
          int id = rs.getInt(1);
          obj.setId(id);
        }
      }
      else{
        throw new RuntimeException("No rows affected!");
      }
    }
    catch(Exception e){
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public void update(Department obj) {
     PreparedStatement st = null;
    try{
      st = conn.prepareStatement("UPDATE department SET DepartmentId = ?, DepartmentName = ? WHERE Id = ?", PreparedStatement.RETURN_GENERATED_KEYS);

      st.setInt(1, obj.getId());
      st.setString(2, obj.getName());

      st.executeUpdate();
    }
    catch(Exception e){
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public void deleteById(Integer id) {
     PreparedStatement st = null;
    try{
      st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

      st.setInt(1, id);

      st.executeUpdate();
    }
    catch(Exception e){
      throw new RuntimeException(e.getMessage());
    }
  }

  @Override
  public Department findById(Integer id) {

    PreparedStatement st = null;
    ResultSet rs = null;

    try{
      st = conn.prepareStatement("SELECT department.* WHERE department.Id = ?");

      st.setInt(1, id);
      rs = st.executeQuery();

      if(rs.next()){
        Department dep = instantiateDepartment(rs);

        return dep;
      }
      return null;
    }
    catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Department> findAll() {
    PreparedStatement st = null;
    ResultSet rs = null;

    try{
      st = conn.prepareStatement("SELECT department ORDER BY Name");

      rs = st.executeQuery();

      List<Department> list = new ArrayList<>();
      
      while(rs.next()){

        Department dep = new Department(rs.getInt("departmentId"), rs.getString("departmentName"));

        list.add(dep);
      }
      return null;
    }
    catch(Exception e){
      throw new RuntimeException(e);
    }
  }
  private Department instantiateDepartment(ResultSet rs) throws Exception{
    Department dep = new Department();

    dep.setId(rs.getInt("DepartmentId"));
    dep.setName(rs.getString("DepartmentName"));

    return dep;
  }
}
