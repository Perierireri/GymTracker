package com.example.mamdosctejjebanejaplikacji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCharacter {
    public String name = "";
    public CharacterClass characterClass = CharacterClass.WARRIOR;
    public int strength = 5;
    public int endurance = 5;
    public int agility = 5;
    public int energy = 5;
    public int level = 1;
    public int xp = 0;
    public EquipmentSlots equipment = new EquipmentSlots();
    public List<Skill> skills = new ArrayList<>();
    public List<Badge> badges = new ArrayList<>();
    public List<Milestone> milestones = new ArrayList<>();
    public Map<String, Integer> maxLifts = new HashMap<>();
    public List<WorkoutEntry> workoutHistory = new ArrayList<>();

    public UserCharacter() {}

    // Derived stats
    public int getTotalWorkouts() { return workoutHistory.size(); }
    public int getTotalWeightLifted() {
        int sum = 0;
        for (WorkoutEntry entry : workoutHistory) {
            if (entry.type.equals("Bench Press") || entry.type.equals("Squat") || entry.type.equals("Deadlift")) {
                for (WorkoutSet set : entry.sets) sum += set.weight;
            }
        }
        return sum;
    }
    public int getTotalDistanceRun() {
        int sum = 0;
        for (WorkoutEntry entry : workoutHistory) {
            if (entry.type.equals("Run")) {
                for (WorkoutSet set : entry.sets) sum += set.weight;
            }
        }
        return sum;
    }
    public int getAverageWorkoutValue() {
        int total = 0;
        for (WorkoutEntry entry : workoutHistory) {
            for (WorkoutSet set : entry.sets) total += set.weight;
        }
        return workoutHistory.size() > 0 ? total / workoutHistory.size() : 0;
    }
    public int getBestLift() {
        int best = 0;
        for (WorkoutEntry entry : workoutHistory) {
            if (entry.type.equals("Bench Press") || entry.type.equals("Squat") || entry.type.equals("Deadlift")) {
                for (WorkoutSet set : entry.sets) {
                    if (set.weight > best) best = set.weight;
                }
            }
        }
        return best;
    }
    public int getWorkoutStreak() {
        return calculateWorkoutStreak();
    }
    private int calculateWorkoutStreak() {
        if (workoutHistory.isEmpty()) return 0;
        List<String> dates = new ArrayList<>();
        for (WorkoutEntry entry : workoutHistory) {
            if (!dates.contains(entry.date)) dates.add(entry.date);
        }
        dates.sort((a, b) -> b.compareTo(a)); // descending
        int streak = 1;
        for (int i = 1; i < dates.size(); i++) {
            java.time.LocalDate prev = java.time.LocalDate.parse(dates.get(i-1));
            java.time.LocalDate curr = java.time.LocalDate.parse(dates.get(i));
            if (prev.minusDays(1).equals(curr)) streak++; else break;
        }
        return streak;
    }

    // --- Getters and Setters for JSON/object mapping ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public CharacterClass getCharacterClass() { return characterClass; }
    public void setCharacterClass(CharacterClass characterClass) { this.characterClass = characterClass; }
    public int getStrength() { return strength; }
    public void setStrength(int strength) { this.strength = strength; }
    public int getEndurance() { return endurance; }
    public void setEndurance(int endurance) { this.endurance = endurance; }
    public int getAgility() { return agility; }
    public void setAgility(int agility) { this.agility = agility; }
    public int getEnergy() { return energy; }
    public void setEnergy(int energy) { this.energy = energy; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }
    public EquipmentSlots getEquipment() { return equipment; }
    public void setEquipment(EquipmentSlots equipment) { this.equipment = equipment; }
    public List<Skill> getSkills() { return skills; }
    public void setSkills(List<Skill> skills) { this.skills = skills; }
    public List<Badge> getBadges() { return badges; }
    public void setBadges(List<Badge> badges) { this.badges = badges; }
    public List<Milestone> getMilestones() { return milestones; }
    public void setMilestones(List<Milestone> milestones) { this.milestones = milestones; }
    public Map<String, Integer> getMaxLifts() { return maxLifts; }
    public void setMaxLifts(Map<String, Integer> maxLifts) { this.maxLifts = maxLifts; }
    public List<WorkoutEntry> getWorkoutHistory() { return workoutHistory; }
    public void setWorkoutHistory(List<WorkoutEntry> workoutHistory) { this.workoutHistory = workoutHistory; }
}
