package com.github.enesusta.validator.core;

import com.github.enesusta.validator.email.Email;
import com.github.enesusta.validator.max.Max;
import com.github.enesusta.validator.min.Min;
import com.github.enesusta.validator.positive.Positive;
import com.github.enesusta.validator.size.Size;

public class Student {

    @Positive
    @Max(max = 15)
    private int stduID;

    @Size(min = 3, max = 10)
    private String stduName;

    @Size(min = 3, max = 10)
    private String stduSurname;

    @Min(min = 15)
    private String stduAddress;

    @Email
    @Size(min = 3, max = 6)
    private String stduEmail;

    @Max(max = 100)
    private byte stduNote;

    public void setStduID(int stduID) {
        this.stduID = stduID;
    }

    public void setStduName(String stduName) {
        this.stduName = stduName;
    }

    public void setStduSurname(String stduSurname) {
        this.stduSurname = stduSurname;
    }

    public void setStduAddress(String stduAddress) {
        this.stduAddress = stduAddress;
    }

    public void setStduNote(byte stduNote) {
        this.stduNote = stduNote;
    }

    public String getStduEmail() {
        return stduEmail;
    }

    void setStduEmail(String stduEmail) {
        this.stduEmail = stduEmail;
    }
}