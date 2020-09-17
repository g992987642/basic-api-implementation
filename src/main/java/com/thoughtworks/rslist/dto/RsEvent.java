package com.thoughtworks.rslist.dto;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class RsEvent {
    public interface RsViewWithoutUser {}

    public interface RsViewWithUser extends RsViewWithoutUser {}

    @NotEmpty
    private String eventName;
    @NotEmpty
    private String keyWord;
    @Valid
    @NotNull
    private UserDto userDto;
    @NotNull
    private int userId;

    public RsEvent(String eventName, String keyWord) {
        this.eventName = eventName;
        this.keyWord = keyWord;
    }

    public RsEvent(@NotEmpty String eventName, @NotEmpty String keyWord, @Valid @NotNull UserDto userDto) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userDto = userDto;
    }

    @JsonView(RsViewWithoutUser.class)
    public String getEventName() {
        return eventName;
    }

    @JsonView(RsViewWithoutUser.class)
    public String getKeyWord() { return keyWord; }

    @JsonView(RsViewWithUser.class)
    public UserDto getUserDto() {
        return userDto;
    }
    @JsonView(RsViewWithUser.class)
    public int getUserId() {
        return userId;
    }
}
