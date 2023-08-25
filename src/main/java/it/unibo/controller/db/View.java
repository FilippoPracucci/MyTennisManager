package it.unibo.controller.db;

import java.util.List;
import java.util.Optional;

public interface View<V, K> {

    /**
     * @return the name of the view
     */
    String getViewName();
    
    /**
     * Finds an object in the view with the given primary key
     * @param primaryKey the primary key used to search the object in the underlying database
     * @return an empty optional if there is no object with the given ID in the database
     */
    Optional<V> findByPrimaryKey(final K primaryKey);
    
    /**
     * @return a list with all the rows of the database 
     */
    List<V> findAll();
}
