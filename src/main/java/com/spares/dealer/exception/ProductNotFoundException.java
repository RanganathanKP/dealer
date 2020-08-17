package com.spares.dealer.exception;

import net.bytebuddy.implementation.bind.annotation.Super;

public class ProductNotFoundException extends RuntimeException{

   public ProductNotFoundException(){
       super();
   }
}
