package com.example.mamdosctejjebanejaplikacji;

import java.util.Arrays;
import java.util.List;

public class ExerciseLibrary {
    public static final List<Exercise> all = Arrays.asList(
        new Exercise("Bench Press", BodyPart.CHEST),
        new Exercise("Push Up", BodyPart.CHEST),
        new Exercise("Pull Up", BodyPart.BACK)
        // Add more as needed
    );
}
