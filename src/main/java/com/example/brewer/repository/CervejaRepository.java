    package com.example.brewer.repository;

    import com.example.brewer.model.Cerveja;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.List;

    @Repository
    public interface CervejaRepository extends JpaRepository<Cerveja, Long> {

        Page<Cerveja> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    }
