package model.dao.impl;

import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
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
  }
  
  @Override
  public void update(Seller obj) {    
  }
  
  @Override
  public void deleteById(Integer id) {    
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
    return null;
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
