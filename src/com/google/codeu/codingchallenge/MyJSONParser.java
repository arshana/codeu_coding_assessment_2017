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

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class MyJSONParser implements JSONParser {
	private MyJSON root;
	
	// Given a string that should be a valid JSON-lite object encoded as a string
	// return the parsed object. If for any reason the string is found to be
	// invalid, the method should throw an IOException.
	@Override
	public JSON parse(String in) throws IOException {
		// TODO: implement this	
		root = new MyJSON();
		in = in.replace("\\", "\\\\");
		in = in.replace("\b", "\\b");
		in = in.replace("\t", "\\t");
		in = in.replace("\n", "\\n");
		in = in.replace("\f", "\\f");
		in = in.replace("\r", "\\r");
		in = in.replace("\'", "\\'");
		in = in.replaceAll("\\\"", "\\\\\\\"");
		root = (MyJSON) parseObject(in);
		return root;
	}
	
	private JSON parseObject(String in) throws IOException{
		in = in.trim();
		if(!in.startsWith("{") || !in.endsWith("}")){
			throw new IOException();
		}
		in = in.substring(1, in.length() - 1).trim();
		if(in.length() == 0){
			return new MyJSON();
		}
		//Ideally, I would find a better way to split in since the key, itself, could contain the string I'm splitting at.
		//If I have more time, I'll try to figure out a better way to handle this.
		String[] splitObject = in.split("\\\"\\s*:\\s*", 2);
		if(splitObject.length != 2){
			throw new IOException();
		}
		String key = parseString(splitObject[0] + "\\\"");
		Object val = parseValue(splitObject[1]);
		MyJSON temp = new MyJSON();
		if(val instanceof JSON){
			temp.setObject(key, (JSON) val);
		}
		else if(val instanceof String){
			temp.setString(key, (String) val);
		}
		else{
			throw new IOException();
		}
		return temp;
	}
	
	private String parseString(String in) throws IOException{
		in = in.trim();
		String regex = "^\\\\\"(?=(\\\\t)*.*(\\\\t)+.*)|(?=(\\\\n)*.*(\\\\n)+.*)|(?=(\\\\\")*.*(\\\\\")+.*)|(?=(\\\\)*.*(\\\\)+.*).*\\\\\"$";		 
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(in);
		if(!matcher.matches()){
			throw new IOException();
		}
		
		in = in.replace("\\b", "\b");
		in = in.replace("\\t", "\t");
		in = in.replace("\\n", "\n");
		in = in.replace("\\f", "\f");
		in = in.replace("\\r", "\r");
		in = in.replace("\\'", "\'");
		in = in.replaceAll("\\\\\\\"", "\\\"");
		in = in.replace("\\\\", "\\");
		return in.substring(1, in.length() - 2);
	}
	
	//can either direct to parseObject of parseStringValue
	//if directs to parseObject, needs to separate by commas (could have multiple key-value pairs)
	private Object parseValue(String in) throws IOException{
		in = in.trim();
		String regex = "^\\\\\"(?=(\\\\t)*.*(\\\\t)+.*)|(?=(\\\\n)*.*(\\\\n)+.*)|(?=(\\\\\")*.*(\\\\\")+.*)|(?=(\\\\)*.*(\\\\)+.*).*\\\\\"$";		 
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(in);
		if(matcher.matches()){
			in = in.replace("\\\\", "\\");
			in = in.replace("\\b", "\b");
			in = in.replace("\\t", "\t");
			in = in.replace("\\n", "\n");
			in = in.replace("\\f", "\f");
			in = in.replace("\\r", "\r");
			in = in.replace("\\'", "\'");
			in = in.replaceAll("\\\\\\\"", "\\\"");
			return in.substring(1, in.length() - 1);
		}
		
		else if(in.startsWith("{") && in.endsWith("}")){
			//now check if there are multiple key-value pairs
			String[] pairs = in.split("\\s*,\\s*\\\\\\\"");
			if(pairs.length > 1){
				//remove curly braces
				pairs[0] = pairs[0].substring(1);
				pairs[pairs.length - 1] = pairs[pairs.length - 1].substring(0, pairs[pairs.length - 1].length() - 1);
				
				MyJSON temp = new MyJSON();

				for(int i = 0; i < pairs.length; i++){
					if(!pairs[i].contains(":")){
						throw new IOException();
					}
					if(i > 0){
						pairs[i] = "\\\"" + pairs[i];
					}
					//Ideally, I would find a better way to split in since the key, itself, could contain the string I'm splitting at.
					//If I have more time, I'll try to figure out a better way to handle this.
					String[] splitObject = pairs[i].split("\\\"\\s*:\\s*", 2);
					if(splitObject.length != 2){
						throw new IOException();
					}
					String key = parseString(splitObject[0] + "\\\"");
					Object val = parseValue(splitObject[1]);

					
					if(val instanceof JSON){
						temp.setObject(key, (JSON) val);
					}
					else if(val instanceof String){
						temp.setString(key, (String) val);
					}
					else{
						throw new IOException();
					}
				}
				return temp;
			}
			else{
				return parseObject(in);
			}
		}
		else{
			throw new IOException();
		}
	}
}