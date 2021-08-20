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
import java.util.ArrayList;
import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    public String merkleRoot;
    public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private long timestamp;
    private int nonce; // for random number .

    public Block(String previousHash) {
        this.previousHash = previousHash;
        
        this.timestamp = new Date().getTime();
        this.hash = calculateHash();
        
    }
    
    public String calculateHash(){
        String calculatedHash = HashUtil.applySha256(
                previousHash +
                Long.toString(timestamp) + 
                Integer.toString(nonce) +
                merkleRoot
                );
        return calculatedHash;
    }
    
    public void mineBlock(int difficulty){
        System.out.println("Mining new Block.........................................");
        merkleRoot = HashUtil.getMerkleRoot(transactions);
        String target = HashUtil.getDificultyString(difficulty); // for diffculty starting with 0's
        while(!hash.substring( 0, difficulty).equals(target)) {
        	nonce ++;
                hash = calculateHash();
	}
	System.out.println("Block Mined!!! : " + hash);
    }
    
    //Add transactions to this block
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
	if(transaction == null) return false;		
        if((previousHash != "0")) {
            if((transaction.processTransaction() != true)) {
                System.out.println("Transaction failed to process. Discarded.");
		return false;
            }
	}
	transactions.add(transaction);
	System.out.println("Transaction Successfully added to Block");
	return true;
    }
    
}
