package com.demoDatajpa_Mapping_.Hibernate.demoDatajpa_Mapping_.Hibernate.config;


import com.demoDatajpa_Mapping_.Hibernate.demoDatajpa_Mapping_.Hibernate.model.User;
import org.springframework.batch.item.ItemProcessor;

// IN this we have to create our own processor Class
//then we have to implement the ItemProcessor interface

// in the interface we use generics as ItemProcessor<ModelClass, ModelClass>
public class UserItemProcessor implements ItemProcessor<User, User>{

    // It is overriding the Process() method of ItemProcessor interface which is just implemented
    @Override
    // Takes the return type of Model Class
    // Takes input as object of the Model Class and returns the same
    public User process(User user) throws Exception{
        return user;
    }
}
