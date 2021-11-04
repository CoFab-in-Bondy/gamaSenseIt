SELECT 'Starting data.sql' AS 'State';

INSERT INTO sensor_metadata VALUES
    (1, ':', 'desc 1', 'big', 'meta-sensor', 'v1.0')
;

INSERT INTO sensor VALUES
    (1, 'sensor-1.0', 'oops ...', 0, 1.2, 1.3, 'sensor-1', 'first sensor', 1)
;

SELECT 'End data.sql' AS 'State';