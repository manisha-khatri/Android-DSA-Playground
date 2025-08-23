package com.example.ds.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FindAllPossibleRecipesfromGivenSupplies {

    /**
     * Topological Sort (Kahn's Algo.)
     */
    static public List<String> findAllRecipes(String[] recipes, List<List<String>> ingredients, String[] supplies) {
        Map<String, List<String>> graph = new HashMap<>();
        Map<String, Integer> indegree = new HashMap<>();

        for(int i=0; i<recipes.length; i++) {
            indegree.put(recipes[i], ingredients.get(i).size());
            for(String in: ingredients.get(i)) {
                if(!graph.containsKey(in)) {
                    graph.put(in, new ArrayList<>());
                }
                graph.get(in).add(recipes[i]);
            }
        }

        Queue<String> supplyQ = new LinkedList<>();
        for(String supply: supplies) supplyQ.offer(supply);

        List<String> createdRecipes = new ArrayList<>();

        while(!supplyQ.isEmpty()) {
            String supply = supplyQ.poll();

            if(graph.containsKey(supply)) {
                for(String recipe: graph.get(supply)) {
                    indegree.put(recipe, indegree.get(recipe)-1);
                    if(indegree.get(recipe) == 0) {
                        createdRecipes.add(recipe);
                        supplyQ.offer(recipe);
                    }
                }
            }
        }
        return createdRecipes;
    }


    static public List<String> findAllRecipes2(String[] recipes, List<List<String>> ingredients, String[] supplies) {
        Set<String> available = new HashSet<>();
        for(int i=0; i<supplies.length; i++) {
            available.add(supplies[i]);
        }

        Queue<Integer> recipeQ = new LinkedList<>();
        for(int i=0; i<recipes.length; i++) {
            recipeQ.offer(i);
        }

        List<String> createdRecipes = new ArrayList<>();
        int lastSize = -1;
        while(available.size() > lastSize) {
            lastSize = available.size();
            int recipeQSize = recipeQ.size();

            while(recipeQSize -- > 0) {
                int i = recipeQ.poll();
                boolean canCreate = true;

                for(String ingredient: ingredients.get(i)) {
                    if(!available.contains(ingredient)) {
                        canCreate = false;
                        break;
                    }
                }

                if(!canCreate) {
                    recipeQ.add(i);
                } else {
                    createdRecipes.add(recipes[i]);
                    available.add(recipes[i]);
                }
            }
        }
        return createdRecipes;
    }

    public static void main(String[] args) {
        String[] recipes = {"bread","sandwich","burger"};

        List<List<String>> ingredients = new ArrayList<>();
        ingredients.add(Arrays.asList("yeast","flour"));
        ingredients.add(Arrays.asList("bread","meat"));
        ingredients.add(Arrays.asList("sandwich","meat","bread"));

        String[] supplies = {"yeast","flour","meat"};

        List<String> result = findAllRecipes(recipes, ingredients, supplies);
        System.out.println(result); // Expected: [bread, sandwich, burger]
    }
}
