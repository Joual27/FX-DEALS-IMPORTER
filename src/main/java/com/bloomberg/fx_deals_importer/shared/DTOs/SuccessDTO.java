package com.bloomberg.fx_deals_importer.shared.DTOs;

public record SuccessDTO<T>(String status , String message , T data) {
}
