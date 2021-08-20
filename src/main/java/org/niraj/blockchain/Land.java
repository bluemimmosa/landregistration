/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.niraj.blockchain;

/**
 *
 * @author Dell
 */
public class Land {
    public int lId;
    public String district;
    public String mun;
    public String ward;
    public int sheetNo;
    public String subSheetNo;
    public int kittaNo;
    public double area; //in sq meter

    public Land(int lId,String district, String mun, String ward, int sheetNo, String subSheetNo, int kittaNo, double area) {
        this.lId = lId;
        this.district = district;
        this.mun = mun;
        this.ward = ward;
        this.sheetNo = sheetNo;
        this.subSheetNo = subSheetNo;
        this.kittaNo = kittaNo;
        this.area = area;
    }

    @Override
    public String toString() {
        return "Land{" + "lId=" + lId + ", district=" + district + ", mun=" + mun + ", ward=" + ward + ", sheetNo=" + sheetNo + ", subSheetNo=" + subSheetNo + ", kittaNo=" + kittaNo + ", area=" + area + '}';
    }
    
    

    public int getlId() {
        return lId;
    }

    public void setlId(int lId) {
        this.lId = lId;
    }
    
    
    
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getMun() {
        return mun;
    }

    public void setMun(String mun) {
        this.mun = mun;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public int getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(int sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getSubSheetNo() {
        return subSheetNo;
    }

    public void setSubSheetNo(String subSheetNo) {
        this.subSheetNo = subSheetNo;
    }

    public int getKittaNo() {
        return kittaNo;
    }

    public void setKittaNo(int kittaNo) {
        this.kittaNo = kittaNo;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
    
    
    
    
    
}
