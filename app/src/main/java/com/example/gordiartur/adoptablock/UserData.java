package com.example.gordiartur.adoptablock;

import android.app.Application;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A global user database class to handle all user data
 *
 * usage example: public int value = 3;
 * int getValue = ((UserData) getApplicationContext()).value;
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
     * Firebase database paths
     */
    public String userPath; // Firebase path to "user" node
    private String blockPath; // Firebase path to "block" node

    /**
     * Firebase database nodes
     */
    private String userNameNode; // Firebase path to "user_name" node
    private String emailNode; // Firebase path to "email" node
    private String organizationNode; // Firebase path to "organization" node
    private String blockNameNode; // Firebase path to "block_name" node

    /**
     * Firebase full path references
     */
    private DatabaseReference userNameReference;
    private DatabaseReference emailReference;
    private DatabaseReference organizationNameReference;
    private DatabaseReference blockNameReference;

    /**
     * User values
     */
    private String userName;
    private String userEmail;
    private String blockName;
    private String organizationName;

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

        if (firebaseUser == null) {
            return;
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();
        uid = firebaseUser.getUid();
        userPath = "users";
        blockPath = "block";
        userNameNode = "user_name";
        emailNode = "email";
        organizationNode = "organization";
        blockNameNode = "block_name";

        userNameReference = mDatabase.child(userPath).child(uid).child(userNameNode);
        emailReference = mDatabase.child(userPath).child(uid).child(emailNode);
        blockNameReference = mDatabase.child(userPath).child(uid).child(blockPath).child(blockNameNode);
        organizationNameReference = mDatabase.child(userPath).child(uid).child(organizationNode);
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
     * @return DatabaseSerference organizationReference
     */
    public DatabaseReference getOrganizationNameReference() {
        return organizationNameReference;
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
     * Return currenly authenticated user's organization name
     * @return organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }
}
