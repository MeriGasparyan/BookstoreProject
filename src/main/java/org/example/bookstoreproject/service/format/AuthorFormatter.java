package org.example.bookstoreproject.service.format;

import org.example.bookstoreproject.enums.Role;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AuthorFormatter {

    public static Map<String, List<Role>> formatAuthor(String authorString) {
        Map<String, List<Role>> authorRolesMap = new LinkedHashMap<>();
        String[] words = authorString.split(",");
        for (int i = 0; i < words.length; i++) {
            String[] oneAuthorInfo = words[i].split("\\(");
            List<Role> roles = new ArrayList<>();
            if (oneAuthorInfo.length == 1) {
                roles.add(Role.AUTHOR);
                authorRolesMap.put(oneAuthorInfo[0].trim(), roles);
            }
            else{
                String name = "";
                for (String component : oneAuthorInfo) {
                    if(!component.trim().endsWith(")")){
                        name = component.trim();
                    }

                    else{
                        String role;
                        role = component.trim().substring(0, component.length()-1).trim();
                        System.out.println(role);
                        roles.add(Role.fromString(role));
                    }
                }
                authorRolesMap.put(name, roles);
            }

        }
        return authorRolesMap;
    }

}
