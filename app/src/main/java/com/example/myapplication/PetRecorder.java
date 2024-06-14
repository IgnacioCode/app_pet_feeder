package com.example.myapplication;

import android.content.Context;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PetRecorder {

    private static final String FILE_NAME = "pets.dat";

    public static void savePetsToFile(Context context, List<Pet> pets) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(pets);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Método para cargar una lista de Pet desde un archivo
    public static List<Pet> loadPetsFromFile(Context context) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        List<Pet> pets = new ArrayList<>();
        try {
            fis = context.openFileInput(FILE_NAME);
            ois = new ObjectInputStream(fis);
            pets = (List<Pet>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return pets;
    }
}
