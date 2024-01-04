package com.example.contentproviderdemo;

class MyTodo {

    private MyTodo(){

    }

    static MyTodo myTodo = null;
    static MyTodo getSingltonObject() {
         if (myTodo == null){
            myTodo  = new MyTodo();
         }

         return myTodo;
    }
}