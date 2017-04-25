// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

//Turning MyJSON into a HashMap-style class
final class MyJSON extends HashMap implements JSON {
  @Override
  public JSON getObject(String name) {
    // TODO: implement this
    if(!isEmpty()){
      //return (JSON) get(name);
      return getObject(name, this);
    }
    return null;
  }
  
  private JSON getObject(String name, MyJSON temp){
    if(temp.containsKey(name) && (temp.get(name) instanceof JSON)){
      return (JSON) get(name);
    }
    else if(!temp.containsKey(name)){
      Set<String> allKeys = temp.keySet();
      for(String key : allKeys){
        if(temp.get(key) instanceof JSON){
          JSON check = temp.getObject(key).getObject(name);
          if(check != null){
            return check;
          }
        }
      }
    }
    return null;
  }

  @Override
  public JSON setObject(String name, JSON value) {
    // TODO: implement this
    if(!setObject(name, value, this)){
      put(name, value);
    }
    return this;
  }
  
  private boolean setObject(String name, JSON value, MyJSON temp){
    if(temp.containsKey(name)){
      temp.put(name, value);
      return true;
    }
    Set<String> allKeys = temp.keySet();
    for(String key : allKeys){
      if(temp.get(key) instanceof JSON){
        boolean check = ((MyJSON) temp.get(key)).setObject(name, value, (MyJSON)temp.get(key));
        if(check){
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public String getString(String name) {
    // TODO: implement this
    if(!isEmpty()){
      //return (JSON) get(name);
      return getString(name, this);
    }
    return null;
  }
  
  private String getString(String name, MyJSON temp){
    if(temp.containsKey(name) && (temp.get(name) instanceof String)){
      return (String) get(name);
    }
    else if(!temp.containsKey(name)){
      Set<String> allKeys = temp.keySet();
      for(String key : allKeys){
        if(temp.get(key) instanceof JSON){
          String check = temp.getObject(key).getString(name);
          if(check != null){
            return check;
          }
        }
      }
    }
    return null;
  }

  @Override
  public JSON setString(String name, String value) {
    // TODO: implement this
    if(!setString(name, value, this)){
      put(name, value);
    }
    return this;
  }
  
  private boolean setString(String name, String value, MyJSON temp){
    if(temp.containsKey(name)){
      temp.put(name, value);
      return true;
    }
    Set<String> allKeys = temp.keySet();
    for(String key : allKeys){
      if(temp.get(key) instanceof JSON){
        boolean check = ((MyJSON) temp.get(key)).setString(name, value, (MyJSON)temp.get(key));
        if(check){
          return true;
        }
      }
    }
    return false;
  }

  @Override
  //wants the keys, NOT the values
  public void getObjects(Collection<String> names) {
    // TODO: implement this
    getObjects(names, this);
  }
  
  private void getObjects(Collection<String> names, MyJSON temp){
    if(!temp.isEmpty()){
      Set<String> allKeys = temp.keySet();
      for(String key : allKeys){
        if(temp.get(key) instanceof JSON){
          names.add(key);
          temp.getObject(key).getObjects(names);
        }
      }
    }
  }

  @Override
  //wants keys, NOT values
  public void getStrings(Collection<String> names) {
    // TODO: implement this
    getStrings(names, this);
  }
  
  private void getStrings(Collection<String> names, MyJSON temp){
    if(!temp.isEmpty()){
      Set<String> allKeys = temp.keySet();
      for(String key : allKeys){
        if(temp.get(key) instanceof String){
          names.add(key);
        }
        else{
          temp.getObject(key).getStrings(names);
        }
      }
    }
  }
}