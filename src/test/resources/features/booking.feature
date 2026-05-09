# language: en
Feature: Gestión de Reservas
  Como usuario deportista
  Quiero realizar una reserva de cancha
  Para asegurar mi espacio de práctica deportiva

  Scenario: Realizar una reserva exitosa
    Given que existe un usuario registrado con email "deportista@test.com"
    And existe una cancha deportiva llamada "Cancha Central"
    When el usuario solicita una reserva para mañana de 10:00 a 12:00
    Then la respuesta del sistema debe indicar que la reserva fue creada
    And la reserva debe estar guardada en la base de datos
