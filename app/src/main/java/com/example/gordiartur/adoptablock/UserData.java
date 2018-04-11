package com.example.gordiartur.adoptablock;

import android.app.Application;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * A global user database class to handle all user data
 *
 *
 * usage example:
 *
 *   private UserData userData;
 *   userData = ((UserData) getApplicationContext());
 *   String name = userData.getUserName();
 *
 */
public class UserData extends Application {
    /**
     * Firebase Authentication instance
     * used to access current user information
     */
    private FirebaseUser firebaseUser;

    /**
     * Firebase Database instance
     * used to access all database
     */
    private DatabaseReference mDatabase;

    /**
     * User ID key used to access current user's info
     */
    private String uid;

    /**
     * Firebase user database paths
     */
    public String userPath; // Firebase path to "user" node
    private String userBlockPath; // Firebase path to "user/uid/block" node

    /**
     * Firebase block database paths
     */
    private String blocksPath; // Firebase path to "blocks" node

    /**
     * Firebase user database nodes
     */
    private String userNameNode; // Firebase path to "user_name" node
    private String emailNode; // Firebase path to "email" node
    private String organizationNode; // Firebase path to "organization" node
    private String blockNameNode; // Firebase path to "block_name" node

    /**
     * Firebase block database nodes
     */
    private String blockAdoptedByNode; // Firebase path to "blockAdoptedBy" node

    /**
     * Firebase full user path references
     */
    private DatabaseReference userNameReference;
    private DatabaseReference emailReference;
    private DatabaseReference organizationNameReference;
    private DatabaseReference blockNameReference;

    /**
     * Firebase full block path references
     */
    private DatabaseReference blockAdoptedByReference;

    /**
     * User values
     */
    private String userName;
    private String userEmail;
    private String blockName;
    private String organizationName;

    /**
     * Total block info
     */
    private int blockAdoptedBy;

    private HashSet<String> blockList;
    private HashMap<String, Integer> adoptedByList;

    /**
     * Create a static UserData object
     */
    public UserData() {
        System.out.println("User Data created");
    }

    /**
     * Set all user information based on user's authentication
     *
     * NOTE: This needs to be called once, explicitly, as user authenticates after all static
     *     objects have been initialized
     */
    public void setUserData() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set blocks references
        blocksPath = "blocks";
        blockAdoptedByNode = "total_adopted_by";

        // Create block list ArrayList
        blockList = new HashSet<>();
        adoptedByList = new HashMap<>();

        // If user is authenticated
        // Set user references
        if (firebaseUser != null) {
            uid = firebaseUser.getUid();
            userPath = "users";
            userBlockPath = "block";
            userNameNode = "user_name";
            emailNode = "email";
            organizationNode = "organization";
            blockNameNode = "block_name";

            userNameReference = mDatabase.child(userPath).child(uid).child(userNameNode);
            emailReference = mDatabase.child(userPath).child(uid).child(emailNode);
            blockNameReference = mDatabase.child(userPath).child(uid).child(userBlockPath).child(blockNameNode);
            organizationNameReference = mDatabase.child(userPath).child(uid).child(organizationNode);
        }
    }

    /**
     * Checks if the user is authenticated. Returns true if yes, else returns false
     * @return boolean
     */
    public boolean isAuthenticated() {
        return firebaseUser != null;
    }

    /**
     * Set currently authenticated user's username
     * @param userName username
     */
    public void setUserName(String userName) {
        this.userName = userName;
        userNameReference.setValue(userName);
    }

    /**
     * Set currently authenticated user's email
     * @param email email
     */
    public void setUserEmail(String email) {
        this.userEmail = email;
        emailReference.setValue(email);
    }

    /**
     * Set currently authenticated user's adopted block name
     * @param blockName adopted block name
     */
    public void setBlockName(String blockName) {
        this.blockName = blockName;
        blockNameReference.setValue(blockName);
    }

    /**
     * Set currently authenticated user's organization name
     * @param organizationName organization name
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
        organizationNameReference.setValue(organizationName);
    }

    /**
     * Increase current number of adopted blocks by one
     */
    public void incrementBlockAdoptedBy(String block) {
        blockAdoptedByReference = mDatabase.child(blocksPath).child(block).child(blockAdoptedByNode);
        int adoptedBy = adoptedByList.get(block);
        blockAdoptedByReference.setValue(++adoptedBy);
        adoptedByList.remove(block);
        adoptedByList.put(block, adoptedBy);
    }

    /**
     * Decrease current number of adopted blocks by one
     */
    public void decrementBlockAdoptedBy(String block) {
        blockAdoptedByReference = mDatabase.child(blocksPath).child(block).child(blockAdoptedByNode);
        int adoptedBy = adoptedByList.get(block);
        if (adoptedBy != 0) {
            blockAdoptedByReference.setValue(--adoptedBy);
            adoptedByList.remove(block);
            adoptedByList.put(block, adoptedBy);
        }
    }

    /**
     * Add all existing blocks to block list
     * @param block block name
     */
    public void addBlockToList(String block) {
        blockList.add(block);
    }

    /**
     * Add all existing counters to block list
     * @param blockName block name
     * @param adoptedBy int counter
     */
    public void addAdoptedBy(String blockName, int adoptedBy) {
        adoptedByList.put(blockName,adoptedBy);
    }

    /**
     * Returns number of people adopting the select block
     * @param blockName block name
     * @return counter
     */
    public int getAdoptedByCounter(String blockName) {
        return adoptedByList.get(blockName);
    }

    /**
     * Returns true if a block already exists in the firebase
     * @param block block name
     * @return true if exists
     */
    public boolean isBlockInList(String block) {
        return blockList.contains(block);
    }

    /**
     * Return currenly authenticated user's username
     * @return username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Return currenly authenticated user's email
     * @return userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Return currenly authenticated user's adopted block name
     * @return blockName
     */
    public String getBlockName() {
        return blockName;
    }

    /**
     * Return currently authenticated user's organization name
     * @return organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Return total number of adopted blocks
     * @return blockAdoptedBy
     */
    public int getBlockAdoptedBy() {
        return blockAdoptedBy;
    }

    /**
     * Return userNameReference used in retrieving data from firebase
     * @return DatabaseReference userNameReference
     */
    public DatabaseReference getUserNameReference() {
        return userNameReference;
    }

    /**
     * Return blockNameReference used in retrieving data from firebase
     * @return DatabaseReference blockNameReference
     */
    public DatabaseReference getBlockNameReference() {
        return blockNameReference;
    }

    /**
     * Return emailReference used in retrieving data from firebase
     * @return DatabaseReference emailReference
     */
    public DatabaseReference getEmailReference() {
        return emailReference;
    }

    /**
     * Return organizationNameReference used in retrieving data from firebase
     * @return DatabaseReference organizationReference
     */
    public DatabaseReference getOrganizationNameReference() {
        return organizationNameReference;
    }
}
