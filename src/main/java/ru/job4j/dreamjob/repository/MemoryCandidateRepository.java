package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@ThreadSafe
public class MemoryCandidateRepository implements CandidateRepository {

    private final AtomicInteger nextId = new AtomicInteger(1);

    private final Map<Integer, Candidate> candidates = new HashMap<>();

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Emil' Mustafaev", "Description for candidate 1", 1, 0));
        save(new Candidate(0, "Ivan Ivanov", "Description for candidate 2", 1, 0));
        save(new Candidate(0, "Petr Petrov", "Description for candidate 3", 2, 0));
        save(new Candidate(0, "Andrey Andreev", "Description for candidate 4", 2, 0));
        save(new Candidate(0, "Stas Stasov", "Description for candidate 5", 3, 0));
        save(new Candidate(0, "Alexey Alexseev", "Description for candidate 6", 3, 0));
    }

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId.incrementAndGet());
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public boolean deleteById(int id) {
        return candidates.remove(id) != null;
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(),
                (id, oldCandidate) ->
                        new Candidate(oldCandidate.getId(),
                                candidate.getTitle(),
                                oldCandidate.getDescription(),
                                oldCandidate.getCityId(),
                                oldCandidate.getFileId())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}

