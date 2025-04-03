DELETE FROM nc_components WHERE component_id = 'resilience-agent';
INSERT INTO nc_components (component_id,is_full_version,install_date,version,fix_number,build_number)
VALUES ('resilience-agent',1,current_date,'0.9.10',null,null);

INSERT INTO nc_components_history (component_id,is_full_version,install_date,version,fix_number,build_number)
VALUES ('resilience-agent',1,current_date,'0.9.10',null,null);

commit;