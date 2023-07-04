package com.MayankApp.service;

import com.MayankApp.entity.CustomUserDetails;
import com.MayankApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDtlsService implements UserDetailsService {
    @Autowired
    private UserRepository repo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try{
            if(repo.findByEmailAndEnabled(email,true)==null ){
                throw new UsernameNotFoundException("No user");
            }

            else
            {
                return  new CustomUserDetails(repo.findByEmail(email));
            }

        }catch (Exception e){
            System.out.println("Exception thrown");
            e.printStackTrace();
        }
        return null;
    }
}
