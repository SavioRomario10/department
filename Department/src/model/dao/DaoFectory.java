package model.dao;

import model.dao.impl.SellerDaoJDBC;
import model.dao.impl.DepartmentDaoJDBC;

public class DaoFectory {

  public static SellerDao createSellerDao() {
    return new SellerDaoJDBC();
  }

  public static DepartmentDao createDepartmentDao() {
    return new DepartmentDaoJDBC();
  }
}
