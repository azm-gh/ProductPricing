package org.jakub

import java.util.Map.Entry
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


import groovy.json.*
import org.codehaus.groovy.runtime.StringGroovyMethods

def products = [
        ["A", "G1", 20.1],
        ["B", "G2", 98.4],
        ["C", "G1", 49.7],
        ["D", "G3", 35.8],
        ["E", "G3", 105.5],
        ["F", "G1", 55.2],
        ["G", "G1", 12.7],
        ["H", "G3", 88.6],
        ["I", "G1", 5.2],
        ["J", "G2", 72.4]
]


// contains information about Category classification based on product Cost
// [Category, Cost range from (inclusive), Cost range to (exclusive)]
// i.e. if a Product has Cost between 0 and 25, it belongs to category C1
def category = [
        ["C3", 50, 75],
        ["C4", 75, 100],
        ["C2", 25, 50],
        ["C5", 100, null],
        ["C1", 0, 25]]

// contains information about margins for each product Category
// [Category, Margin (either percentage or absolute value)]
def margins = [
        "C1" : "20%",
        "C2" : "30%",
        "C3" : "0.4",
        "C4" : "50%",
        "C5" : "0.6"]



//removing percentages
Map<String, String> map1 = new ConcurrentHashMap<>(margins);
map1.each { Entry<String, String> val ->
    if (val.value.endsWith("%")) {
        val.value = val.value.substring(0, val.value.length() - 1)
    }
}
//Changing types in a map to String, Double
Map<String, Double> newMap = map1.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey,e -> Double.parseDouble(e.getValue())));

//Normalizing margin numbers
newMap.each { entry ->
    if(entry.value < 1) {
        newMap.put(entry.key, newMap.get(entry.key) + 1)
    }
    else if(entry.value > 1 ) {
        newMap.put(entry.key, newMap.get(entry.key) / 100 + 1)
    }
}

//Next step: fit each product in category and multiply it by given margin
COST_MAX_AMOUNT = 99999;

products.each {   val ->
    if(val[2] >= 0 && val[2] < 25) {
        val[2] = val[2] * newMap.get("C1")
    }
    else if(val[2] >= 25 && val[2] <50 ) {
        val[2] = val[2] * newMap.get("C2")
    }
    else if(val[2] >= 50 && val[2] <= 75 ) {
        val[2] = val[2] * newMap.get("C3")
    }
    else if(val[2] >= 75 && val[2] < 100 ) {
        val[2] = val[2] * newMap.get("C4")
    }
    else if(val[2] >= 75 && val[2] < COST_MAX_AMOUNT ) {
        val[2] = val[2] * newMap.get("C5")
    }
}

// Create new def result =  Map<String, Double> = [:]
//
//iterate through products. If list[1[ == G1 ->

products.each {
    entry ->
        products[0][2]
}
def sumG1 = 0;
def G1amount = 0;
def sumG2 = 0;
def G2amount = 0;
def sumG3 = 0;
def G3amount = 0;

for(int h = 0; h < products.size(); h++) {
    if(products[h][1] == 'G1') {
        sumG1  = sumG1 + products[h][2]
        G1amount++
    }
    else if(products[h][1] == 'G2') {
        sumG2  = sumG2 + products[h][2]
        G2amount++
    }
    else if(products[h][1] == 'G3') {
        sumG3  = sumG3 + products[h][2]
        G3amount++
    }
}

def averageG1 =  sumG1 / G1amount
def averageG2 = sumG2 / G2amount
def averageG3 =  sumG3 / G3amount
println products.size()
println products
def result = ["G1":averageG1, "G2":averageG2, "G3":averageG3]
println result

//Todo:
// 0. Commit existing code as is
// 1. Round numbers to one decimal point.
// 2. Fix bug for G3 - wrong result
// 3. access of val in the if else closure - replace with something better
// 4. investigate how to utilize collection.inject
// 5. investigate how to rewrite the for loop in more groovy style closure
// 6. look into: make the category look-up performance effective
// 7. look into: Assume there can be a large number of products and a large number of categories.
// 8. Review documentation https://groovy-lang.org/groovy-dev-kit.html#Collections-Lists
// 9. Look into how to use Collect
// 10. Fix null boundary !