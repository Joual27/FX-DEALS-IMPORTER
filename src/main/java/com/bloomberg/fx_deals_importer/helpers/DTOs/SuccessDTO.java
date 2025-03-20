package com.bloomberg.fx_deals_importer.helpers.DTOs;

import java.time.LocalDateTime;

public record SuccessDTO<T>(String status , String message , LocalDateTime timestamps, T data) {
}
