package model.dao.impl;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SellerDaoJDBC implements SellerDao{

  private Connection conn;

  public SellerDaoJDBC() {}
  public SellerDaoJDBC(Connection conn) {
    this.conn = conn;
  }

  @Override
  public void insert(Seller obj) {   
    
    PreparedStatement st = null;
    try{
      st = conn.prepareStatement("INSERT INTO seller (Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

      st.setString(1, obj.getName());
      st.setString(2, obj.getEmail());
      st.setDate(3, new Date(obj.getBirthDate().getTime()));
      st.setDouble(4, obj.getBaseSalary());
      st.setInt(5, obj.getDepartment().getId());

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
  public void update(Seller obj) {  
      
    PreparedStatement st = null;
    try{
      st = conn.prepareStatement("UPDATE seller SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? WHERE Id = ?", PreparedStatement.RETURN_GENERATED_KEYS);

      st.setString(1, obj.getName());
      st.setString(2, obj.getEmail());
      st.setDate(3, new Date(obj.getBirthDate().getTime()));
      st.setDouble(4, obj.getBaseSalary());
      st.setInt(5, obj.getDepartment().getId());
      st.setInt(6, obj.getId());

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
      st = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

      st.setInt(1, id);

      st.executeUpdate();
    }
    catch(Exception e){
      throw new RuntimeException(e.getMessage());
    }
  }
  
  @Override
  public Seller findById(Integer id) {    

    PreparedStatement st = null;
    ResultSet rs = null;

    try{
      st = conn.prepareStatement("SELECT seller.*, department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE seller.Id = ?");

      st.setInt(1, id);
      rs = st.executeQuery();

      if(rs.next()){
        Department dep = instantiateDepartment(rs);
        Seller seller = instantiateSeller(rs, dep);

        return seller;
      }
      return null;
    }
    catch(Exception e){
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<Seller> findAll() {    
    
    PreparedStatement st = null;
    ResultSet rs = null;

    try{
      st = conn.prepareStatement("SELECT seller.*, department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id ORDER BY Name");

      rs = st.executeQuery();

      List<Seller> list = new ArrayList<>();
      Map<Integer, Department> map = new HashMap<>();

      while(rs.next()){

        Department dep = map.get(rs.getInt("DepartmentId"));

        if(dep == null){
          dep = instantiateDepartment(rs);
          map.put(rs.getInt("DepartmentId"), dep);
        }

        Seller seller = instantiateSeller(rs, dep);
        list.add(seller);
      }
      return null;
    }
    catch(Exception e){
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Seller> findByDepartment(Department department) {    
    PreparedStatement st = null;
    ResultSet rs = null;

    try{
      st = conn.prepareStatement("SELECT seller.*, department.Name as DepName FROM seller INNER JOIN department ON seller.DepartmentId = department.Id WHERE DepartmentId = ? ORDER BY Name");

      st.setInt(1, department.getId());
      rs = st.executeQuery();

      List<Seller> list = new ArrayList<>();
      Map<Integer, Department> map = new HashMap<>();

      while(rs.next()){

        Department dep = map.get(rs.getInt("DepartmentId"));

        if(dep == null){
          dep = instantiateDepartment(rs);
          map.put(rs.getInt("DepartmentId"), dep);
        }

        Seller seller = instantiateSeller(rs, dep);
        list.add(seller);
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
    dep.setName(rs.getString("DepName"));
    return dep;
  }
  private Seller instantiateSeller(ResultSet rs, Department dep) throws Exception{
    Seller seller = new Seller();

    seller.setId(rs.getInt("Id"));
    seller.setName(rs.getString("Name"));
    seller.setEmail(rs.getString("Email"));
    seller.setBirthDate(rs.getDate("BirthDate"));
    seller.setBaseSalary(rs.getDouble("BaseSalary"));
    seller.setDepartment(dep);

    return seller;
  }
}
