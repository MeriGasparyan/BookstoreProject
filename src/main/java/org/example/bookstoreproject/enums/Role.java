package org.example.bookstoreproject.enums;

public enum Role {
    AUTHOR,
    GOODREADS_AUTHOR,
    ILLUSTRATOR,
    EDITOR,
    TRANSLATOR,
    INTRODUCTION,
    PREFACE,
    AFTERWORD,
    ANNOTATIONS,
    COMMENTARY,
    FOREWORD,
    NOTES,
    CONTRIBUTOR,
    NARRATOR,
    COLORIST,
    LETTERER,
    ADAPTED_BY,
    ORIGINAL_STORY_BY,
    ILLUSTRATIONS,
    UNKNOWN;

    public static Role fromString(String str) {
        switch (str.trim().toLowerCase()) {
            case "goodreads author": return GOODREADS_AUTHOR;
            case "illustrator": case "illustrations": return ILLUSTRATOR;
            case "editor": return EDITOR;
            case "translator", "übersetzer": return TRANSLATOR;
            case "introduction": return INTRODUCTION;
            case "preface": return PREFACE;
            case "afterword": return AFTERWORD;
            case "annotations": return ANNOTATIONS;
            case "commentary": return COMMENTARY;
            case "foreword": return FOREWORD;
            case "notes": return NOTES;
            case "contributor": return CONTRIBUTOR;
            case "narrator": return NARRATOR;
            case "colorist": return COLORIST;
            case "letterer": return LETTERER;
            case "adapted by": return ADAPTED_BY;
            case "original story by": return ORIGINAL_STORY_BY;
            default: return UNKNOWN;
        }
    }
}
