package com.clanz.base.domain;

@lombok.Data
@lombok.AllArgsConstructor
@lombok.NoArgsConstructor
public class KMSObject {
    byte[] content;
    byte[] key;
}
