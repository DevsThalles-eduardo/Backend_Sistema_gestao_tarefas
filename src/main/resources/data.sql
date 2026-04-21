

INSERT INTO prioridade (nivel)
SELECT v FROM (VALUES ('ALTA'), ('MÉDIA'), ('BAIXA')) AS t(v)
WHERE NOT EXISTS (SELECT 1 FROM prioridade);

INSERT INTO categoria (tipo_categoria)
SELECT v FROM (VALUES ('Geral')) AS t(v)
WHERE NOT EXISTS (SELECT 1 FROM categoria);

-- =========================================
-- VIEW
-- =========================================
CREATE OR REPLACE VIEW tarefas_por_prioridade AS
SELECT 
    p.nivel AS prioridade, 
    COUNT(t.id_tarefa) AS total
FROM prioridade p
LEFT JOIN tarefa t 
    ON t.id_prioridade = p.id_prioridade
GROUP BY p.nivel;