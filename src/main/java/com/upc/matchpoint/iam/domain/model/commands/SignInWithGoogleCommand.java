package com.upc.matchpoint.iam.domain.model.commands;

public record SignInWithGoogleCommand(String idToken, String role) {
}
