package org.hanghae.markethub.domain.purchase.dto;
public record IamportResponseDto(int code, String message, Response response
){
    public record Response(String access_token, long now, long expired_at
    ) {

    }
}