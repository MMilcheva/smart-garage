package com.example.smart_garage.models;

import java.util.Optional;

public class DiscountFilterOptions {
    private Optional<String> discountName;
    private Optional<String> username;
    private Optional<Boolean> isArchived;

    private Optional<String> sortBy;

    private Optional<String> sortOrder;

    public DiscountFilterOptions(String discountName, String username, Boolean isArchived, String sortBy, String sortOrder) {
        this.discountName = Optional.ofNullable(discountName);
        this.username = Optional.ofNullable(username);
        this.isArchived = Optional.ofNullable(isArchived);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getDiscountName() {
        return discountName;
    }

    public void setDiscountName(Optional<String> discountName) {
        this.discountName = discountName;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public Optional<Boolean> getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Optional<Boolean> isArchived) {
        this.isArchived = isArchived;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }
}
