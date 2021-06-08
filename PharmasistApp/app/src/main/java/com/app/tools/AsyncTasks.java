package com.app.tools;

public class AsyncTasks {

    /*
        This class specially for the urls of the server
     */

    private final String ROOT_URL = "https://pharmasist-app.herokuapp.com";
    public final String REGISTER_USER = ROOT_URL + "/Users/Add";
    public final String UPDATE_USER = ROOT_URL + "/Users/Update";
    public final String UPDATE_PRODUCT = ROOT_URL + "/Products/Update";
    public final String FIND_USER = ROOT_URL + "/Users/Check";
    public final String ADD_PRODUCT = ROOT_URL + "/Products/Add";
    public final String DELETE_USER = ROOT_URL + "/Users/Delete/";
    public final String DELETE_PRODUCT = ROOT_URL + "/Products/Delete/";
    public final String GET_USER_PRODUCT = ROOT_URL + "/ProductsUser/";
    public final String SEARCH_PRODUCT = ROOT_URL + "/ProductsName/";
    public final String SEARCH_PRODUCT_LIST = ROOT_URL + "/ProductsList/";
    public final String CHECK_CONNECTION = ROOT_URL + "/CheckConnection";

}
