package cn.osworks.aos.system.modules.controller.chulinprint;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class DataTest implements Serializable {
   public int iValue;
   public double dbValue;
   public String strValue;
   public Date	dtValue;
   public boolean bValue;
   
   public DataTest(int _iValue, double _dbValue, String _strValue, Date _dtValue, boolean _bValue) {
	   iValue = _iValue;
	   dbValue = _dbValue;
	   strValue = _strValue;
	   dtValue = _dtValue;
	   bValue = _bValue;
   }
}
