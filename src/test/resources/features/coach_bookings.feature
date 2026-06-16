# language: en
Feature: Gestión de Sesiones de Entrenamiento
  Como entrenador independiente
  Quiero visualizar las reservas que tengo asignadas
  Para organizar mi agenda de clases

  Scenario: Entrenador consulta sus reservas asignadas
    Given existe un entrenador llamado "Coach Pro" con especialidad "Tenis"
    And el entrenador tiene 2 reservas asignadas en el sistema
    When el entrenador consulta su lista de reservas
    Then el sistema debe retornar exactamente 2 reservas
    And todas las reservas deben pertenecer al entrenador "Coach Pro"
