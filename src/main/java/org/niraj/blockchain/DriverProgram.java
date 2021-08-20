/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.niraj.blockchain;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class DriverProgram {
    
    public static BlockChain bc = new BlockChain();
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        Scanner sc = new Scanner(System.in);
        int choice;
        do{
            showMenu();
            System.out.println("Enter your choice: ");
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    createLalPurja();
                    //writeToFile();
                    break;
                case 2:
                    registerNewLand();
                    //writeToFile();
                    break;
                case 3:
                    transferLand();
                    //writeToFile();
                    break;
                case 0:
                    System.out.println("Exiting......");
                    //writeToFile();
                    break;
                case 4:
                    findOwner();
                    break;
                case 5:
                    printLandTransactionsTrace();
                    break;
                default:
                    System.out.println("Enter Valid choice.");
            }
        }while(choice != 0);
    }
    
//    public static void writeToFile() throws FileNotFoundException, IOException{
//        FileOutputStream f = new FileOutputStream(new File("myblkchain.txt"));
//        ObjectOutputStream o = new ObjectOutputStream(f);
//        o.writeObject(bc);
//        o.close();
//        f.close();
//    }
//    
//    public static void readFromFile() throws FileNotFoundException, IOException, ClassNotFoundException{
//        FileInputStream f = new FileInputStream("myblkchain.txt");
//        ObjectInputStream i = new ObjectInputStream(f);
//        bc = (BlockChain)i.readObject();
//    }
    
    public static void findOwner(){
        int lId;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Land Owner Details.");
        System.out.println("Enter Land ID: ");
        lId = sc.nextInt();
        sc.nextLine();
        if(bc.findLand(lId) == null){
            System.out.println("Cannot find the land.");
            return;
        }
        System.out.println(bc.findCurrentOwner(lId));
    }
    
    public static void printLandTransactionsTrace(){
        int lId;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Land Owner Details.");
        System.out.println("Enter Land ID: ");
        lId = sc.nextInt();
        sc.nextLine();
        if(bc.findLand(lId) == null){
            System.out.println("Cannot find the land.");
            return;
        }
        bc.printOwnershipChange(lId);
    }
    
    public static void showMenu(){
        System.out.println("");
        System.out.println("Welcome to BlockChain Land Management System");
        System.out.println("");
        System.out.println("1. Create new Lal Purja");
        System.out.println("2. Register new Land");
        System.out.println("3. Transfer Land Registration");
        System.out.println("4. Find the current Ownership of Land");
        System.out.println("5. Print the detail transfer of particular Land");
        System.out.println("0. Quit");
    }
    
    public static void createLalPurja(){
        String name, ctzn;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Lal Purja Creation.");
        System.out.println("Name of Owner: ");
        name = sc.nextLine();
        System.out.println("Citizenship No: ");
        ctzn = sc.nextLine();
        LalPurja l = new LalPurja(name, ctzn);
        bc.purjas.add(l);
        System.out.println("Your Private Key is: "+HashUtil.getStringFromKey(l.privateKey));
        System.out.println("Your Public Key is: "+HashUtil.getStringFromKey(l.publicKey));
        System.out.println("Please keep those safely. Once lost cannot be recovered.");
        System.out.println("Lal Purja created and successfully added into the blockchain.");
    }
    
    public static void registerNewLand(){
        int lId, sheetNo, kittaNo;
        String district, mun, ward, subSheetNo;
        double area;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Land Registration");
        System.out.println("land ID (unique): ");
        lId = sc.nextInt();
        sc.nextLine();
        System.out.println("District: ");
        district = sc.nextLine();
        System.out.println("Municipality: ");
        mun = sc.nextLine();
        System.out.println("Ward: ");
        ward = sc.nextLine();
        System.out.println("Sheet No: ");
        sheetNo = sc.nextInt();
        sc.nextLine();
        System.out.println("Sub Sheet No: ");
        subSheetNo = sc.nextLine();
        System.out.println("Kitta No: ");
        kittaNo = sc.nextInt();
        sc.nextLine();
        System.out.println("Area: ");
        area = sc.nextDouble();
        sc.nextLine();
        
        Land l = new Land(lId, district, mun, ward, sheetNo, subSheetNo, kittaNo, area);
        bc.lands.add(l);
        System.out.println("New Land Registered Successfully.....");
        
    }
    
    public static void transferLand(){
        String ownerName;
        int lId;
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to Land transfer");
        System.out.println("land ID (unique): ");
        lId = sc.nextInt();
        sc.nextLine();
        if(bc.findLand(lId) == null){
            System.out.println("Cannot find the land.");
            return;
        }
        System.out.println("Enter receivers Name:");
        ownerName = sc.nextLine();
        PublicKey pk = bc.findPublicKey(ownerName);
        if(pk == null){
            System.out.println("Cannot find the Lal Purja for the given owner.");
            return;
        }
        
        
        Block blk = new Block(bc.blockchain.get(bc.blockchain.size()-1).hash);
        if(blk.addTransaction(BlockChain.purjas.get(0).passLand(pk, bc.findLand(lId)))){
            bc.addBlock(blk);
            //System.out.println("Land No: "+lId+" transfered to: "+bc.findCurrentOwner(lId));
            System.out.println("");
        }
        else
            System.out.println("Transaction must be initiated by malpot");
        
        
        
    }
    
}
        