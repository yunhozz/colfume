package colfume.api.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Failure implements Result {

    private String message;
}