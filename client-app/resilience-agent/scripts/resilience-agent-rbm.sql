DELETE FROM components_versions WHERE component_id = 'resilience-agent';
                                    INSERT INTO components_versions (COMPONENT_ID, COMPONENT_TYPE, INSTALL_DATE, VERSION, PACKAGE_TYPE)
                                    VALUES ('resilience-agent', 1, sysdate, '0.9.10_fixnull', 'FULL');
                                    INSERT INTO components_versions_history (COMPONENT_ID, COMPONENT_TYPE, INSTALL_DATE, VERSION, PACKAGE_TYPE)
                                    VALUES ('resilience-agent', 1, sysdate, '0.9.10_fixnull', 'FULL');

                                    commit;