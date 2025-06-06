package ru.slisarenko.springpractick.config.sequrity.jwt.dto;

public record InformationUsefulFromToken(String accessToken,
                                         String accessTokenExpiry,
                                         String RefreshToken,
                                         String RefreshTokenExpiry) {
}
