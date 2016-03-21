package com.support.android.designlibdemo;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Mocked API for listing {@link Cheese}s.
 *
 * Created by wessel on 21/03/16.
 */
public class CheeseApi {
    /**
     * List the {@link Cheese}s.
     *
     * @param amount The amount of {@link Cheese}s to list.
     * @return The listed {@link Cheese}s.
     */
    @NonNull
    public static List<Cheese> listCheeses(int amount) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException exception) {
        } finally {
            return Cheeses.getRandomSublist(amount);
        }
    }
}
