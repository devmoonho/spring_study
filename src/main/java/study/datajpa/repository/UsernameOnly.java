package study.datajpa.repository;

/**
 * UsernameOnly
 */
public interface UsernameOnly {
    // @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}

