package org.mooc.mail;

import java.util.List;

public class UserGroup {

    private List<String> users;

    public void addUser(String username){
        users.add(username);
    }
}
