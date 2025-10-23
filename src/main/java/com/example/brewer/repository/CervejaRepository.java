    package com.example.brewer.repository;

    import com.example.brewer.model.Cerveja;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository
    public interface CervejaRepository extends JpaRepository<Cerveja, Long> {
    }
