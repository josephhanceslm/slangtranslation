(ns slangtranslation.core-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [slangtranslation.core :refer :all]))

            (def dict-1 {"hizzle" "house"})

            (def dict-2 {"hizzle" "house"
                         "homie" "friend"})
            
            (defn split-string
              "Splits a string into a vector"
              [input-string]
                (str/split 
                  (str/replace input-string #"[.,!?;:]" " $0") #" "))
                                                    
            (defn join-vector
              "Joins the elements of a vector into a string"
              [input-vector]
              (str/replace 
                (str/join " " input-vector) #"(?: )([.,!?;:])" "$1"))  
                  
            (defn translate-words
              "Translate any words from the dictionary"
              [input-string dictionary]
              (map (fn [w] (if (contains? dictionary w) (dictionary w) w)) input-string))
                  
            (defn translate-thread
              "Same as translate but uses the Clojure threading macros"
              [input-string dictionary]
              (-> input-string
                (split-string)
                (translate-words dictionary)
                (join-vector)))

            (defn translate
              "Handles punctuation, [.,!?;:], as well"
              [input-string dictionary]              
              (join-vector (map (fn [w] (if (contains? dictionary w) (dictionary w) w)) (split-string input-string))))

            (defn translate-old
              "Passes tests but fails if there is punctuation"
              [input-string dictionary]
              (str/join " " (map (fn [w] (if (contains? dictionary w) (dictionary w) w)) (str/split input-string #" "))))            
            
            (deftest a-test
              (testing "Test with a single word to be translated"
                (is (= "In the house" (translate-thread "In the hizzle" dict-1)))))

            (deftest b-test
              (testing "Test with two words to be translated"
                (is (= "In the house with my friend" (translate-thread "In the hizzle with my homie" dict-2)))))   
                  
            (deftest c-test
              (testing "Test with one word to be translated and punctuation"
                (is (= "What’s in the house?" (translate-thread "What’s in the hizzle?" dict-2)))))                   
