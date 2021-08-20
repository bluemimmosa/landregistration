/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.niraj.blockchain;

import java.io.Serializable;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Dell
 */
public class BlockChain implements Serializable{
    
    public ArrayList<Block> blockchain = new ArrayList<>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();
    public int difficulty = 5;  
    public static ArrayList<LalPurja> purjas = new ArrayList<>();
    public ArrayList<Land> lands = new ArrayList<>();
    public Transaction genesisTransaction;
    public LalPurja malpot;
    
    public BlockChain(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        
        //LalPurja malpot = new LalPurja("Malpot", "Sankhu");
        malpot = new LalPurja("Malpot", "Sankhu");
        
        purjas.add(malpot);
        
        genesisTransaction = new Transaction(malpot.publicKey, malpot.publicKey, new Land(0, "", "", "0", 0, "0", 0, 000.00), null);
        genesisTransaction.generateSignature(malpot.privateKey);
        genesisTransaction.transactionId = "0";
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.receiver, genesisTransaction.land, genesisTransaction.transactionId));
        UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));
        
        System.out.println("Creating and Mining Genesis block... ");
	Block genesis = new Block("0");
	genesis.addTransaction(genesisTransaction);
	addBlock(genesis);
    }
    
    public Land findLand(int lId){
        for(Land l: lands){
            if(l.lId == lId)
                return l;
        }
        return null;
    }
    public Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>(); //a temporary working list of unspent transactions at a given block state.
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for (int i = 1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("#Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("#Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("#This block hasn't been mined");
                return false;
            }

            //loop thru blockchains transactions:
            TransactionOutput tempOutput;
            for (int t = 0; t < currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if (!currentTransaction.verifySignature()) {
                    System.out.println("#Signature on Transaction(" + t + ") is Invalid");
                    return false;
                }

                for (TransactionInput input : currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if (tempOutput == null) {
                        System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
                        return false;
                    }

                    if (!input.UTXO.land.equals(tempOutput.land)) {
                        System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for (TransactionOutput output : currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if (currentTransaction.outputs.get(0).receiver != currentTransaction.receiver) {
                    System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
                    return false;
                }
                if (currentTransaction.outputs.get(1).receiver != currentTransaction.sender) {
                    System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
                    return false;
                }

            }

        }
        System.out.println("Blockchain is valid");
        return true;
    }

    public void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }

    public void printOwnershipChange(int lId) {
        System.out.println("");
        for (Block b : blockchain) {
            for (Transaction t : b.transactions) {
                if (lId == t.land.getlId()) {
                    PublicKey pk = t.receiver;
                    for (LalPurja l : purjas) {
                        if (pk == l.publicKey) {
                            //System.out.println("Latest Owner found: "+ l.ownerName);
                            System.out.println(l.ownerName);
                            System.out.println("|");
                        }
                    }

                }
            }
        }
        System.out.println("");
    }
    
    public String findCurrentOwner(int lId){
        String oName = "";
        for(Block b: blockchain){
            //System.out.println("This block's hash: "+b.hash);
            for(Transaction t: b.transactions){
                //System.out.println("This transaction id: "+t.transactionId+ " has following landID: "+t.land.getlId());
                
                if(lId == t.land.getlId()){
                    PublicKey pk = t.receiver;
                    for(LalPurja l: purjas){
                        if(pk.equals(l.publicKey)){
                            //System.out.println("Latest Owner found: "+ l.ownerName);
                            oName = l.ownerName;
                        }
                    }
                }
            }
        }
        return oName;
    }
    
    public PublicKey findPublicKey(String owner){
        for(LalPurja l: purjas){
            if(l.ownerName.equals(owner)){
                return l.publicKey;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "BlockChain{" + "blockchain=" + blockchain + ", difficulty=" + difficulty + ", lands=" + lands + ", genesisTransaction=" + genesisTransaction + ", malpot=" + malpot + '}';
    }
    
}
